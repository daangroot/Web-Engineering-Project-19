import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CarriersAirportComponent } from './carriers-airport.component';

describe('CarriersAirportComponent', () => {
  let component: CarriersAirportComponent;
  let fixture: ComponentFixture<CarriersAirportComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CarriersAirportComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CarriersAirportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
