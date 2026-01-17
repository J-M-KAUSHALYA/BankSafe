import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { of, throwError } from 'rxjs';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule, HttpTestingController } from "@angular/common/http/testing";
import { Router } from '@angular/router';
import { DashboardComponent } from '../app/bank/components/dashboard/dashboard.component';
import { Transaction } from '../app/bank/types/Transaction';
import { BankModule } from '../app/bank/bank.module';
import { TransactionComponent } from '../app/bank/components/transaction/transaction.component';
import { BankService } from '../app/bank/services/bank.service';

class MockBankService {
    performTransaction(transaction: Transaction) {
        return of({ success: true });
    }
    getCustomerById() {
        return of({ customerId: 1, name: 'John Doe' });
    }
    getAccountsByUser() {
        return of([{ accountId: 1, balance: 1000, customer: { customerId: 1, name: 'John Doe' } }]);
    }
    getAllTransactionsByCustomerId() {
        return of([{ transactionId: 1, amount: 500 }]);
    }
}

describe('TransactionComponent', () => {
    let component: TransactionComponent;
    let fixture: ComponentFixture<TransactionComponent>;
    let bankService: BankService;
    let router: Router;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [TransactionComponent],
            imports: [ReactiveFormsModule],
            providers: [
                { provide: BankService, useClass: MockBankService },
                { provide: Router, useValue: { navigate: jasmine.createSpy('navigate') } }
            ]
        }).compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(TransactionComponent);
        component = fixture.componentInstance;
        bankService = TestBed.inject(BankService);
        router = TestBed.inject(Router);
        fixture.detectChanges();
    });

    it('should create the component', () => {
        expect(component).toBeTruthy();
    });

    it('should initialize the form on ngOnInit', () => {
        expect(component.transactionForm).toBeDefined();
        expect(component.transactionForm.get('accounts')).toBeTruthy();
        expect(component.transactionForm.get('transactionType')).toBeTruthy();
        expect(component.transactionForm.get('amount')).toBeTruthy();
    });

    it('should fetch accounts for USER role on ngOnInit', () => {
        spyOn(bankService, 'getAccountsByUser').and.callThrough();
        localStorage.setItem('role', 'USER');
        localStorage.setItem('user_id', '1');

        component.ngOnInit();
        expect(bankService.getAccountsByUser).toHaveBeenCalledWith('1');
        expect(component.accounts.length).toBe(1);
    });

    it('should validate the amount field', () => {
        const amountControl = component.transactionForm.get('amount');
        amountControl?.setValue(-1);
        expect(amountControl?.hasError('min')).toBeTrue();

        amountControl?.setValue(100);
        expect(amountControl?.valid).toBeTrue();
    });

    it('should display error message on invalid form submission', () => {
        component.transactionForm.setValue({
            accounts: null,
            transactionType: null,
            amount: null,
        });

        component.onSubmit();

        expect(component.isFormSubmitted).toBeTrue();
        expect(component.transactionSuccess).toBeUndefined();
        expect(component.transactionError).toBeUndefined();
    });

    it('should call performTransaction on valid form submission', fakeAsync(() => {
        spyOn(bankService, 'performTransaction').and.callThrough();

        component.transactionForm.setValue({
            accounts: 1,
            transactionType: 'DEBIT',
            amount: 100,
        });

        component.onSubmit();
        tick();

        expect(bankService.performTransaction).toHaveBeenCalled();
        expect(component.transactionError).toBe('');
        expect(component.transactionSuccess).toBe('Transaction performed successfully');
    }));

    it('should handle errors from performTransaction service', fakeAsync(() => {
        spyOn(bankService, 'performTransaction').and.returnValue(
            throwError({ error: 'Transaction failed' })
        );

        component.transactionForm.setValue({
            accounts: 1,
            transactionType: 'CREDIT',
            amount: 100,
        });

        component.onSubmit();
        tick();

        expect(component.transactionSuccess).toBe('');
        expect(component.transactionError).toBe('Transaction failed');
    }));
});


describe('DashboardComponent', () => {
    let bankService: BankService;
    let component: DashboardComponent;
    let fixture: ComponentFixture<DashboardComponent>;
    let router: Router;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [DashboardComponent],
            imports: [ReactiveFormsModule, HttpClientTestingModule, RouterTestingModule.withRoutes([]), BankModule],
            providers: [{ provide: BankService, useClass: MockBankService }],
        }).compileComponents();

        fixture = TestBed.createComponent(DashboardComponent);
        component = fixture.componentInstance;
        router = TestBed.inject(Router);
        bankService = TestBed.inject(BankService);
        fixture.detectChanges();
    });

    it('should create DashboardComponent', () => {
        expect(component).toBeTruthy();
    });

    it('should load user data if role is USER', () => {
        spyOn(component, 'loadUserData').and.callThrough();
        localStorage.setItem('role', 'USER');
        component.ngOnInit();
        expect(component.loadUserData).toHaveBeenCalled();
    });

});
