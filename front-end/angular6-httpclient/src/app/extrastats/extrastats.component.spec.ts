import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExtrastatsComponent } from './extrastats.component';

describe('ExtrastatsComponent', () => {
  let component: ExtrastatsComponent;
  let fixture: ComponentFixture<ExtrastatsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExtrastatsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExtrastatsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
