// customers.component.spec.ts
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomerTS } from '../app/bank/types/tstypes/Customerts';
import { AccountTS } from '../app/bank/types/tstypes/Accountts';
import { AccountsampleComponent } from '../app/bank/components/accountsample/accountsample.component';
import { CustomersampleComponent } from '../app/bank/components/customersample/customersample.component';

describe('CustomersampleComponent', () => {
  let component: CustomersampleComponent;
  let fixture: ComponentFixture<CustomersampleComponent>;
  let componentAccount: AccountsampleComponent;
  let fixtureAccount: ComponentFixture<AccountsampleComponent>;
  beforeEach(() => {
    TestBed.configureTestingModule({
      // declarations: [CustomersampleComponent],
    });

    fixture = TestBed.createComponent(CustomersampleComponent);
    fixtureAccount = TestBed.createComponent(AccountsampleComponent);
    componentAccount=fixtureAccount.componentInstance;
    component = fixture.componentInstance;
  });
  it('should display account information', () => {
    const account: AccountTS = 
      new AccountTS("1", 1000.00, "1");

    componentAccount.account = account;
    fixtureAccount.detectChanges();

    const compiled = fixtureAccount.nativeElement;

    expect(compiled.textContent).toContain('Account ID: 1');
    expect(compiled.textContent).toContain('Customer ID: 1');
    expect(compiled.textContent).toContain('Balance: $1000.00');
   
  });
  
  it('should display customer details correctly', () => {
    // Arrange
    const mockCustomer:CustomerTS = new CustomerTS(
      'John Doe',
      'john.doe@example.com',
      'john_doe',
      'securepassword',
      'user',
      "123"
  );
 
    // Act
    component.customer = mockCustomer;
    fixture.detectChanges();

    // Assert
    const compiled = fixture.nativeElement;

    expect(compiled.textContent).toContain('Customer Details');
    expect(compiled.textContent).toContain('ID: 123');
    expect(compiled.textContent).toContain('Name: John Doe');
    expect(compiled.textContent).toContain('Email: john.doe@example.com');
    expect(compiled.textContent).toContain('Username: john_doe');
    expect(compiled.textContent).toContain('Password: securepassword');
    expect(compiled.textContent).toContain('Role: user');
  });
});
