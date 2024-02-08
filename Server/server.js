const WebSocket = require('ws');

const wss = new WebSocket.Server({ port: 3500, host: '0.0.0.0' });

const { connectToDatabase, login } = require('./database');

wss.on('connection', (ws) => {
  console.log('Client connected');

  ws.on('login', (username, password) => {
    console.log(username + ' try to login');
    let result = login(username, password);
    ws.send(result);
  });

  ws.on('message', (message) => {
    console.log(`Received: ${message}`);
    // Handle the incoming message and send a response if needed
    ws.send('Server received your message');
  });

  ws.on('close', () => {
    console.log('Client disconnected');
  });
});

connectToDatabase();

console.log('WebSocket server is running on port 3500');

