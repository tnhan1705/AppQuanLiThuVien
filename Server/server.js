const WebSocket = require('ws');

const wss = new WebSocket.Server({ port: 3500, host: '0.0.0.0' });
var clients = new Set(); // Maintain a set of connected clients

// require func database
const { connectToDatabase, login, getAllBooks, getAllReceipts, order,updateReceiptStatus,updateReceipt} = require('./database');

// require enum
const { LOG_TYPE, EVENT } = require('./constant');

wss.on('connection', (ws) => {
  clients.add(ws);
  ws.on('message', (message) => {
    try {
      const data = JSON.parse(message);

      switch(data.event){
        case EVENT.LOGIN:
          console.log('Received REMARK event with status:', data.username, 'and id:', data.password);
          handleLogin(ws, data.username, data.password);
          break;
        case EVENT.GET_DATA:
          handleGetData(ws, data.username)
          break;
        case EVENT.ORDER:
          handleOrder(ws, data.receipt, data.username)
          break;
        case EVENT.REMARK:  
          console.log('Received REMARK event with status:' + EVENT.REMARK );
          if (data.receipt && data.username) {
            console.log('Received REMARK event with status:');
            handleReceipt(ws, data.receipt, data.username);
          } else {
            console.error('Invalid data received for REMARK event');
            // Gửi thông báo lỗi trở lại cho client
            ws.send(JSON.stringify({ event: EVENT.REMARK, result: 'error', message: 'Invalid data received' }));
          }
          break; 
        case EVENT.UPDATE:
          console.log('Received REMARK event with status:' + EVENT.UPDATE );
          if (data.receipt && data.username) {
    
              console.log('Received REMARK event with status:');
              handleUpdateReceipt(ws, data.receipt, data.username);
            } else {
              console.error('Invalid data received for Update event');
              // Gửi thông báo lỗi trở lại cho client
              ws.send(JSON.stringify({ event: EVENT.UPDATE, result: 'error', message: 'Invalid data received' }));
            }
            break;
s
          default:
          console.error('Error parsing JSON:', error.message);
          console.log(LOG_TYPE.ERROR + `Unhandled event: ${data.event}`);
      }
      
    } catch (error) {
      console.error('Error parsing message:', error.message);
    }
  });

  ws.on('close', () => {
    
  });
});

async function handleLogin(ws, username, password) {
  console.log(LOG_TYPE.NOTIFY + `${username} tries to login`);
  try {
    const result = await login(username, password);
    // Convert boolean result to string before sending
    ws.send(JSON.stringify({ event: EVENT.LOGIN, result: result.toString() }));
    console.log(LOG_TYPE.NOTIFY + `${username} login ${result === true ? "success" : "failed"}`);
  } catch (error) {
    console.error('Error during login:', error.message);
    ws.send(JSON.stringify({ event: EVENT.LOGIN, result: 'false' }));
  }
}

async function handleGetData(ws, username){
  console.log(LOG_TYPE.NOTIFY + `${username} tries to get data`);
  try {
    const rsAllBooks = await getAllBooks();
    const rsAllReceipt = await getAllReceipts();
    const data = {
      books: rsAllBooks, 
      receipts: rsAllReceipt
    }
    
    ws.send(JSON.stringify({ event: EVENT.GET_DATA, data: JSON.stringify(data) }));
  } catch (error) {
    console.error('Error during get all books:', error.message);
  }
}

async function handleOrder(ws, receipt, username){
  console.log(LOG_TYPE.NOTIFY + `${username} tries to order`);
  try {
    receipt = JSON.parse(receipt);
    receipt.date_start = formatDate(receipt.date_start);
    receipt.date_return = formatDate(receipt.date_return);
    const rsOrder = await order(receipt);

    console.log("Result order: " + rsOrder);
    ws.send(JSON.stringify({ event: EVENT.ORDER, result: rsOrder.toString() }));
  } catch (error) {
    console.error('Error during order:', error.message);
    ws.send(JSON.stringify({ event: EVENT.ORDER, result: 'false' }));
  }
}


async function handleReceipt(ws,receipt,username) {
  console.log(LOG_TYPE.NOTIFY + `${username} tries to mark receipt as returned`);

  try {
    // Gọi hàm cập nhật trạng thái Phiếu trong cơ sở dữ liệu
    receipt = JSON.parse(receipt);
    const success = await updateReceiptStatus( receipt);

    if (success && ws.readyState === WebSocket.OPEN) {
      // Gửi lại thông báo về client rằng Phiếu đã được cập nhật thành công
      ws.send(JSON.stringify({ event: EVENT.REMARK, result: 'true' }));
    } else {
      // Gửi lại thông báo về client nếu cập nhật thất bại hoặc kết nối không hợp lệ
      ws.send(JSON.stringify({ event: EVENT.REMARK, result: 'failed' }));
    }
  } catch (error) {
    console.error('Error during marking receipt as returned:', error.message);
    ws.send(JSON.stringify({ event: EVENT.REMARK, result: 'error' }));
  }
}


async function handleUpdateReceipt(ws,receipt,username) {
  console.log(LOG_TYPE.NOTIFY + `${username} tries to mark receipt as returned`);

  try {
    // Gọi hàm cập nhật trạng thái Phiếu trong cơ sở dữ liệu
    receipt = JSON.parse(receipt);
    const success = await updateReceipt( receipt);

    if (success && ws.readyState === WebSocket.OPEN) {
      // Gửi lại thông báo về client rằng Phiếu đã được cập nhật thành công
      ws.send(JSON.stringify({ event: EVENT.UPDATE, result: 'true' }));
    } else {
      // Gửi lại thông báo về client nếu cập nhật thất bại hoặc kết nối không hợp lệ
      ws.send(JSON.stringify({ event: EVENT.UPDATE, result: 'failed' }));
    }
  } catch (error) {
    console.error('Error during marking receipt as returned:', error.message);
    ws.send(JSON.stringify({ event: EVENT.UPDATE, result: 'error' }));
  }
}



function formatDate(date) {
  // Ensure 'date' is a valid Date object
  if (!(date instanceof Date)) {
    date = new Date(date);
  }

  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');

  return `${year}/${month}/${day} ${hours}:${minutes}:${seconds}`;
}

connectToDatabase();