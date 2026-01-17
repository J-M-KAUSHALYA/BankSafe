import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { TransactionComponent } from '../app/bank/components/transaction/transaction.component';
import { AccountComponent } from '../app/bank/components/account/account.component';

describe('TransactionComponent', () => {
  let component: TransactionComponent;
  let fixture: ComponentFixture<TransactionComponent>;
  let componentAccount: AccountComponent;
  let fixtureAccount: ComponentFixture<AccountComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReactiveFormsModule],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TransactionComponent);
    component = fixture.componentInstance;

    fixture.detectChanges();
    fixtureAccount = TestBed.createComponent(AccountComponent);
    componentAccount = fixtureAccount.componentInstance;

    fixtureAccount.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize the form with controls', () => {
    expect(component.transactionForm).toBeDefined();
    expect(component.transactionForm.get('transaction_id')).toBeDefined();
    expect(component.transactionForm.get('account_id')).toBeDefined();
    expect(component.transactionForm.get('amount')).toBeDefined();
    expect(component.transactionForm.get('transaction_date')).toBeDefined();
  });

  it('should set form controls as required', () => {
    const formControls = ['accountId', 'amount', 'transactionDate','transactionType'];

    formControls.forEach((control) => {
      expect(component.transactionForm.get(control)?.hasError('required')).toBe(true);
    });
  });
  
  it('should pass validation when all fields are valid', () => {
    componentAccount.accountForm.setValue({
      account_id: 1,
      customer_id: 1,
      balance: 1000.0,
    });

    expect(componentAccount.accountForm.valid).toBeTruthy();
  });

  it('should set amount control as invalid if value is less than 0', () => {
    const amountControl = component.transactionForm.get('amount');

    amountControl?.setValue(-1);
    expect(amountControl?.hasError('min')).toBe(true);

    amountControl?.setValue(0);
    expect(amountControl?.hasError('min')).toBe(false);
  });

  

 
  it('should create a Transaction object on form submission', () => {
    component.transactionForm.setValue({
      transactionId: 1,
      accountId: 1,
      amount: 50.00,
      transactionType:"Credit",
      transactionDate: new Date(),
    });

    component.onSubmit();
  });
});
