const WebSocket = require('ws');

const wss = new WebSocket.Server({ port: 3500, host: '0.0.0.0' });
var clients = new Set(); // Maintain a set of connected clients

// require func database
const { connectToDatabase, login, getAllBooks, getAllReceipts } = require('./database');

// require enum
const { LOG_TYPE, EVENT } = require('./constant');

wss.on('connection', (ws) => {
  clients.add(ws);
  ws.on('message', (message) => {
    try {
      const data = JSON.parse(message);

      switch(data.event){
        case EVENT.LOGIN:
          handleLogin(ws, data.username, data.password);
          break;
        case EVENT.GET_DATA:
          handleGetData(ws, data.username)
          break;
        default:
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
    ws.send(JSON.stringify({ event: 'login', result: 'false' }));
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

connectToDatabase();