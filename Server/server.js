const WebSocket = require('ws');

const wss = new WebSocket.Server({ port: 3500, host: '0.0.0.0' });

// require func database
const { connectToDatabase, login } = require('./database');

// require enum
const { LOG_TYPE, EVENT } = require('./constant');

wss.on('connection', (ws) => {

  ws.on('message', (message) => {
    try {
      const data = JSON.parse(message);
      console.log("@ " + data);
      const { event, username, password } = data;

      switch(event){
        case EVENT.LOGIN:
          handleLogin(ws, username, password);
          break;
        default:
          console.log(LOG_TYPE.ERROR + `Unhandled event: ${event}`);
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
    ws.send(JSON.stringify({ event: 'login', result: result.toString() }));
    console.log(LOG_TYPE.NOTIFY + `${username} login ${result === true ? "success" : "failed"}`);
  } catch (error) {
    console.error('Error during login:', error.message);
    ws.send(JSON.stringify({ event: 'login', result: 'false' }));
  }
}

connectToDatabase();