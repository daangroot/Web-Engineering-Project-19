import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TwoAirportsCarrierComponent } from './two-airports-carrier.component';

describe('TwoAirportsCarrierComponent', () => {
  let component: TwoAirportsCarrierComponent;
  let fixture: ComponentFixture<TwoAirportsCarrierComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TwoAirportsCarrierComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TwoAirportsCarrierComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
