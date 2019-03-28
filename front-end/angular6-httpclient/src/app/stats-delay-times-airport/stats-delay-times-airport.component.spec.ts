import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StatsDelayTimesAirportComponent } from './stats-delay-times-airport.component';

describe('StatsDelayTimesAirportComponent', () => {
  let component: StatsDelayTimesAirportComponent;
  let fixture: ComponentFixture<StatsDelayTimesAirportComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StatsDelayTimesAirportComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StatsDelayTimesAirportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
