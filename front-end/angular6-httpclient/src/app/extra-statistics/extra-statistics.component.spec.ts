import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExtraStatisticsComponent } from './extra-statistics.component';

describe('ExtraStatisticsComponent', () => {
  let component: ExtraStatisticsComponent;
  let fixture: ComponentFixture<ExtraStatisticsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExtraStatisticsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExtraStatisticsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
