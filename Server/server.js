const WebSocket = require('ws');

const wss = new WebSocket.Server({ port: 3500, host: '0.0.0.0' });
var clients = new Set(); // Maintain a set of connected clients

// require func database
const { connectToDatabase, login, getAllBooks, getAllReceipts, order, addBook, addUser, checkUsernameExists, changePassword } = require('./database');
// require enum
const { LOG_TYPE, EVENT } = require('./constant');

wss.on('connection', (ws) => {
    clients.add(ws);
    ws.on('message', (message) => {
        try {
            const data = JSON.parse(message);
            switch (data.event) {
                case EVENT.LOGIN:
                    handleLogin(ws, data.username, data.password);
                    break;
                case EVENT.GET_DATA:
                    handleGetData(ws, data.username)
                    break;
                case EVENT.ORDER:
                    handleOrder(ws, data.receipt, data.username)
                    break;
                case EVENT.ADD_USER:
                    handleAddUser(ws, data.user)
                    break;
                case EVENT.CHECK_USERNAME:
                    handleCheckUsername(ws, data.username)
                    break;
                case EVENT.CHANGE_PASSWORD:
                    handleChangePassword(ws, data.username, data.newPassword)
                    break;
                    case EVENT.ADD_BOOK:
                        handleAddBook(ws, data.receipt, data.username)
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
        ws.send(JSON.stringify({ event: EVENT.LOGIN, result: 'false' }));
    }
}

async function handleGetData(ws, username) {
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

async function handleOrder(ws, receipt, username) {
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

async function handleAddUser(ws, user) {
    console.log(LOG_TYPE.NOTIFY + `Received request to add user`);

    try {
        const userData = JSON.parse(user);
        const addUserResult = await addUser(userData);
        console.log("Result of adding user: " + addUserResult);
        ws.send(JSON.stringify({ event: EVENT.ADD_USER, result: addUserResult.toString() }));
    } catch (error) {
        console.error('Error during adding user:', error.message);
        ws.send(JSON.stringify({ event: EVENT.ADD_USER, result: 'false' }));
    }
}
async function handleCheckUsername(ws, username) {
    console.log(LOG_TYPE.NOTIFY + `Received request to check username`);

    try {
        const usernameExists = await checkUsernameExists(username);
        console.log("Result of checking username: " + JSON.stringify(usernameExists));
        ws.send(JSON.stringify({ event: EVENT.CHECK_USERNAME, data: JSON.stringify(usernameExists) }));
    } catch (error) {
        console.error('Error during checking username:', error.message);
        ws.send(JSON.stringify({ event: EVENT.CHECK_USERNAME, result: 'false' }));
    }
}

async function handleChangePassword(ws, username, newPassword) {
    console.log(LOG_TYPE.NOTIFY + `Received request to change password`);

    try {
        console.error('Error during changing password:', username + newPassword);
        // Call the function to change the password
        const changePasswordResult = await changePassword(username, newPassword);

        console.log("Result of changing password: " + changePasswordResult);

        // Send the result back to the client
        ws.send(JSON.stringify({ event: EVENT.CHANGE_PASSWORD, result: changePasswordResult.toString() }));
    } catch (error) {
        console.error('Error during changing password:', error.message);

        // Send the error message back to the client
        ws.send(JSON.stringify({ event: EVENT.CHANGE_PASSWORD, result: 'false', message: error.message }));
    }
}

async function handleAddBook(ws, receipt, username){
  console.log(LOG_TYPE.NOTIFY + `${username} tries to add book`);
  try {
    receipt = JSON.parse(receipt);
    const rsOrder = await addBook(receipt);
    ws.send(JSON.stringify({ event: EVENT.ADD_BOOK, result: rsOrder.toString() }));
  } catch (error) {
    console.error('Error during add book:', error.message);
    ws.send(JSON.stringify({ event: EVENT.ADD_BOOK, result: 'false' }));
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