import { CustomerTS } from '../app/bank/types/tstypes/Customerts';
import { AccountTS } from '../app/bank/types/tstypes/Accountts'; 
import { TransactionTS } from '../app/bank/types/tstypes/Transactionts';

// Using Jasmine for testing
describe('Classes', () => {
  
  describe('CustomerTS Class', () => {
    let customer: CustomerTS;
    let spy: jasmine.Spy;

    beforeEach(() => {
      customer = new CustomerTS("John Doe", "john@example.com", "john_doe", "password123", "User", "1");
      spy = spyOn(console, 'log');
    });

    it('displayInfo method should log correct information', () => {
      customer.displayInfo();
      expect(spy).toHaveBeenCalledWith(jasmine.stringMatching(/Customer\s*ID\s*:\s*1/));
      expect(spy).toHaveBeenCalledWith(jasmine.stringMatching(/Name\s*:\s*John Doe/));
      expect(spy).toHaveBeenCalledWith(jasmine.stringMatching(/Email\s*:\s*john@example.com/));
      expect(spy).toHaveBeenCalledWith(jasmine.stringMatching(/Username\s*:\s*john_doe/));
      expect(spy).toHaveBeenCalledWith(jasmine.stringMatching(/Password\s*:\s*password123/));
      // Add more assertions for other properties
    });
  });

  describe('AccountTS Class', () => {
    let account: AccountTS;
    let spy: jasmine.Spy;

    beforeEach(() => {
      account = new AccountTS("1", 1000.00, "1");
      spy = spyOn(console, 'log');
    });

    it('displayInfo method should log correct information', () => {
      account.displayInfo();
      expect(spy).toHaveBeenCalledWith(jasmine.stringMatching(/Account\s*ID\s*:\s*1/));
      expect(spy).toHaveBeenCalledWith(jasmine.stringMatching(/Customer\s*ID\s*:\s*1/));
      expect(spy).toHaveBeenCalledWith(jasmine.stringMatching(/Balance\s*:\s*1000.00/));
    });
  });

  describe('TransactionTS Class', () => {
    let transaction: TransactionTS;
    let spy: jasmine.Spy;

    beforeEach(() => {
      transaction = new TransactionTS("1", 50.00, new Date(), 1);
      spy = spyOn(console, 'log');
    });

    it('displayInfo method should log correct information', () => {
      transaction.displayInfo();
      expect(spy).toHaveBeenCalledWith(jasmine.stringMatching(/Transaction\s*ID\s*:\s*1/));
      expect(spy).toHaveBeenCalledWith(jasmine.stringMatching(/Account\s*ID\s*:\s*1/));
      expect(spy).toHaveBeenCalledWith(jasmine.stringMatching(/Amount\s*:\s*50.00/));
      expect(spy).toHaveBeenCalledWith(jasmine.stringMatching(/Transaction\s*Date\s*:/));
      // Add more assertions for other properties
    });
  });

});
