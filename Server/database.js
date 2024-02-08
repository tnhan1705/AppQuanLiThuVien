const mysql = require('mysql2/promise');
const fs = require('fs');

let connection;

// Function to establish a MySQL connection
async function connectToDatabase() {
    try {
      if (!connection) {
        connection = await mysql.createConnection({
          host: 'localhost',
          user: 'root',
          password: '123456',
          database: 'quanlithuvien',
        });
        console.log('Connected to MySQL database');
      }
      return connection;
    } catch (error) {
      console.error('Error connecting to MySQL database:', error.message);
      throw error;
    }
}

async function login(username, password){
    try {
        // Connect to the database
        const connection = await connectToDatabase();
    
        // Query to check login credentials
        const query = 'SELECT * FROM quanlithuvien.taikhoan WHERE username = username AND password = password';
    
        // Execute the query with parameters
        const [rows, fields] = await connection.execute(query, [username, password]);
    
        // Check if any rows were returned
        const loginSuccessful = rows.length > 0;
    
        // Close the connection when done
        await connection.end();
        console.log('Connection closed');
    
        return loginSuccessful;
    } catch (error) {
        console.error('An error occurred:', error.message);
        return false; // Return false in case of an error
    }
}

// Export the connectToDatabase function
module.exports = { connectToDatabase, login };
