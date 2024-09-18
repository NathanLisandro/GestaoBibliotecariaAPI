import { createGlobalStyle } from 'styled-components';

const GlobalStyles = createGlobalStyle`
  * {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
  }

  body {
    font-family: Arial, sans-serif;
    background-color: #f4f4f9;
    color: #333;
  }

  a {
    text-decoration: none;
    color: inherit;
  }

  button {
    cursor: pointer;
  }

  table {
    width: 100%;
    border-collapse: collapse;
    margin: 20px 0;
  }

  th, td {
    border: 1px solid #ddd;
    padding: 8px;
  }

  th {
    background-color: #4CAF50;
    color: white;
  }

  tr:nth-child(even) {
    background-color: #f2f2f2;
  }

  tr:hover {
    background-color: #ddd;
  }

  .container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 20px;
  }

  .button {
    background-color: #4CAF50;
    color: white;
    padding: 10px 20px;
    border: none;
    border-radius: 5px;
    margin: 5px;
    transition: background-color 0.3s ease;

    &:hover {
      background-color: #45a049;
    }
  }

  .form-control {
    margin: 10px 0;
    padding: 8px;
    width: 100%;
    border: 1px solid #ccc;
    border-radius: 4px;
  }

  .form-label {
    margin-bottom: 5px;
    display: block;
  }
`;

export default GlobalStyles;
