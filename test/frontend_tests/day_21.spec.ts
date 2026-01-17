import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { CustomersComponent } from '../app/bank/components/customer/customer.component';
import { AccountComponent } from '../app/bank/components/account/account.component';

describe('CustomersComponent', () => {
  let fixture: ComponentFixture<CustomersComponent>;
  let component: CustomersComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CustomersComponent],
      imports: [FormsModule, ReactiveFormsModule],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize the form', () => {
    expect(component.customerForm).toBeDefined();
  });

  it('should have form controls for name, email, username, and password', () => {
    const formControls = ['name', 'email', 'username', 'password'];

    formControls.forEach(control => {
      expect(component.customerForm.get(control)).toBeTruthy();
    });
  });

  it('should initialize the form with empty controls', () => {
    expect(component.customerForm.get('name')?.value).toBe('');
    expect(component.customerForm.get('email')?.value).toBe('');
    expect(component.customerForm.get('username')?.value).toBe('');
    expect(component.customerForm.get('password')?.value).toBe('');
  });

  it('should mark form controls as invalid if they are touched and empty', () => {
    const formControls = ['name', 'email', 'username', 'password'];

    formControls.forEach(control => {
      const input = fixture.debugElement.query(By.css(`#${control}`)).nativeElement;

      input.dispatchEvent(new Event('focus'));
      input.dispatchEvent(new Event('blur'));

      fixture.detectChanges();

      expect(component.customerForm.get(control)?.invalid).toBe(true);
    });
  });

  it('should update customerError for invalid email format on form submission', () => {
    component.customerForm.setValue({
      name: 'name',
      email: 'invalidemail',
      username: 'user',
      password: 'abcd1234',
    });

    component.onSubmit();

    expect(component.customerError).toBe('Please correct the errors in the form.');
  });

  it('should call onSubmit method on form submission', () => {
    spyOn(component, 'onSubmit');
    const form = fixture.debugElement.query(By.css('form')).nativeElement;
    form.dispatchEvent(new Event('submit'));

    expect(component.onSubmit).toHaveBeenCalled();
  });


});

describe('AccountComponent', () => {
  let component: AccountComponent;
  let fixture: ComponentFixture<AccountComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AccountComponent],
      imports: [ReactiveFormsModule],
    });

    fixture = TestBed.createComponent(AccountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should validate accountId as required', () => {
    const accountIdControl = component.accountForm.get('accountId');
    accountIdControl!.setValue(null);

    expect(accountIdControl!.hasError('required')).toBe(true);
  });

  it('should validate balance as non-negative', () => {
    const balanceControl = component.accountForm.get('balance')!;
    balanceControl.setValue(-1);

    expect(balanceControl.hasError('min')).toBe(true);
  });

  it('should pass validation when all fields are valid', () => {
    component.accountForm.setValue({
      accountId: 1,
      customerId: 1,
      balance: 1000.0,
    });

    expect(component.accountForm.valid).toBe(true);
  });
});
