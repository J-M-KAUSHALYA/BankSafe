// form.test.js

const { JSDOM } = require('jsdom');
const { login, register } = require('../../day_14_15/js/validation'); // Replace with the actual filename

// Set up a basic DOM for the tests
const dom = new JSDOM('<!doctype html><html><body></body></html>');
global.document = dom.window.document;

// Mock console.log to capture log messages
console.log = jest.fn();

// Test cases for login function
describe('test', () => {
    test('login function should log correct information - Day 15', () => {
        // Clear previous DOM state
        document.body.innerHTML = '';
    
        // Set up input fields for login
        const usernameInput = document.createElement('input');
        usernameInput.id = 'loginUsername';
        usernameInput.value = 'testUser';
        document.body.appendChild(usernameInput);
    
        const passwordInput = document.createElement('input');
        passwordInput.id = 'loginPassword';
        passwordInput.value = 'testPassword';
        document.body.appendChild(passwordInput);
    
        // Call the login function
        login();
    
        // Check if console.log was called with the correct message
        expect(console.log).toHaveBeenCalledWith('Login clicked. Username: testUser, Password: testPassword');
    });
    
    // Test cases for register function
    test('register function should log correct information - Day 15', () => {
        // Clear previous DOM state
        document.body.innerHTML = '';
    
        // Set up input fields for registration
        const nameInput = document.createElement('input');
        nameInput.id = 'registerName';
        nameInput.value = 'John Doe';
        document.body.appendChild(nameInput);
    
        const emailInput = document.createElement('input');
        emailInput.id = 'registerEmail';
        emailInput.value = 'john@example.com';
        document.body.appendChild(emailInput);
    
        const usernameInput = document.createElement('input');
        usernameInput.id = 'registerUsername';
        usernameInput.value = 'johndoe'; // Use underscores in the username
        document.body.appendChild(usernameInput);
    
        const passwordInput = document.createElement('input');
        passwordInput.id = 'registerPassword';
        passwordInput.value = 'Password123';
        document.body.appendChild(passwordInput);
    
        // Call the register function
        register();
    
        // Check if console.log was called with the correct message
        expect(console.log).toHaveBeenCalledWith('Register clicked. Name: John Doe, Email: john@example.com, Username: johndoe, Password: Password123');
    });
})
