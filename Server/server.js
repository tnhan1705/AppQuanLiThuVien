const WebSocket = require('ws');

const wss = new WebSocket.Server({ port: 3500, host: '0.0.0.0' });


wss.on('connection', (ws) => {
  console.log('Client connected');

  ws.on('message', (message) => {
    console.log(`Received: ${message}`);
    // Handle the incoming message and send a response if needed
    ws.send('Server received your message');
  });

  ws.on('close', () => {
    console.log('Client disconnected');
  });
});

console.log('WebSocket server is running on port 3500');
