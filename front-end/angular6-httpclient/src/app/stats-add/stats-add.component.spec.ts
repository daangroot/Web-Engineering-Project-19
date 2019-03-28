import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StatsAddComponent } from './stats-add.component';

describe('StatsAddComponent', () => {
  let component: StatsAddComponent;
  let fixture: ComponentFixture<StatsAddComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StatsAddComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StatsAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
