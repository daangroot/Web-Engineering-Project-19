import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FlightStatisticsComponent } from './flight-statistics.component';

describe('FlightStatisticsComponent', () => {
  let component: FlightStatisticsComponent;
  let fixture: ComponentFixture<FlightStatisticsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FlightStatisticsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FlightStatisticsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
