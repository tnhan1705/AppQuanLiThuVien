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
          password: 'zed123456@',
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
        const query = 'SELECT * FROM quanlithuvien.taikhoan WHERE username = ? AND password = ?';
    
        // Execute the query with parameters
        const [rows, fields] = await connection.execute(query, [username, password]);
    
        // Check if any rows were returned
        const loginSuccessful = rows.length > 0;
    
        // // Close the connection when done
        // await connection.end();
        // console.log('Connection closed');
    
        return loginSuccessful;
    } catch (error) {
        console.error('An error occurred:', error.message);
        return false; // Return false in case of an error
    }
}

async function getAllBooks() {
  try {
    // Connect to the database
    const connection = await connectToDatabase();

    // Query to get all rows from the 'sach' table
    const query = 'SELECT * FROM quanlithuvien.sach';

    // Execute the query
    const [rows, fields] = await connection.execute(query);

    // Return the result
    return rows;
  } catch (error) {
    console.error('An error occurred while getting all books:', error.message);
    throw error;
  }
}

async function getAllReceipts() {
  try {
    // Connect to the database
    const connection = await connectToDatabase();

    // Query to get all rows from the 'sach' table
    const query = 'SELECT * FROM quanlithuvien.phieu';

    // Execute the query
    const [rows, fields] = await connection.execute(query);

    // Return the result
    return rows;
  } catch (error) {
    console.error('An error occurred while getting all receipt:', error.message);
    throw error;
  }
}

async function order(receipt){
  try {
    // Connect to the database
    const connection = await connectToDatabase();

    const query1 = `INSERT INTO quanlithuvien.phieu 
      (id, id_books, status, first_name, last_name, gender, email, phone, date_start, date_return) 
      VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)`;

    const query2 = `UPDATE quanlithuvien.sach 
    SET inventory_quantity = inventory_quantity - 1 
    WHERE id = ?`;
    
    const { id, id_books, status, first_name, last_name, gender, email, phone, date_start, date_return } = receipt;

    // Execute the query with parameters
    await connection.execute(query1, [id, id_books, status, first_name, last_name, gender, email, phone, date_start, date_return]);

    let arrIds = receipt.id_books.split(",");
    // Loop through each receipt and execute the query
    for (const arrId of arrIds) {
      // Execute the update query with parameter
      await connection.execute(query2, [arrId]);
    }

    // Return the result
    return true;
  } catch (error) {
    console.error('An error occurred while getting all receipt:', error.message);
    throw false;
  }
}

async function updateReceiptStatus( Receipt) {
  try {
    // Connect to the database
    const connection = await connectToDatabase();

    // Query to update the status of the receipt
    const query = 'UPDATE quanlithuvien.phieu SET status = ? WHERE id = ?';
    const  {id, status} = Receipt;
    // Execute the query
    const [result] = await connection.execute(query, [status, id]);

    // Check if the query was successful
    if (result.affectedRows === 1) {
      console.log('Receipt status updated successfully');
      return true;
    } else {
      console.error('Failed to update receipt status');
      return false;
    }
  } catch (error) {
    console.error('An error occurred while updating receipt status:', error.message);
    throw error;
  }
}


async function updateReceipt( Receipt) {
  try {
    // Connect to the database
    const connection = await connectToDatabase();

    // Query to update the status of the receipt
    const query = 'UPDATE quanlithuvien.phieu SET first_name = ?, last_name = ?, gender = ?, email = ?, phone = ?  WHERE id = ?';
    const  {id,first_name,last_name,gender,email,phone} = Receipt;
    // Execute the query
    const [result] = await connection.execute(query, [first_name,last_name,gender,email,phone, id]);

    // Check if the query was successful
    if (result.affectedRows === 1) {
      console.log('Receipt status updated successfully');
      return true;
    } else {
      console.error('Failed to update receipt status');
      return false;
    }
  } catch (error) {
    console.error('An error occurred while updating receipt status:', error.message);
    throw error;
  }
}


// Export the connectToDatabase function
module.exports = { connectToDatabase, login, getAllBooks, getAllReceipts, order, updateReceiptStatus,updateReceipt};
