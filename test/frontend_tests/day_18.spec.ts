import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { CustomersComponent } from '../app/bank/components/customer/customer.component';
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

 
});
