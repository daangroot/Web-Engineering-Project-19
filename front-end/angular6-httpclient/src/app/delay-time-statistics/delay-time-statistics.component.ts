import { Component, OnInit, Input } from '@angular/core';
import { RestService } from '../rest.service';

@Component({
  selector: 'app-delay-time-statistics',
  templateUrl: './delay-time-statistics.component.html',
  styleUrls: ['./delay-time-statistics.component.css']
})
export class DelayTimeStatisticsComponent implements OnInit {
  @Input() airportCode:string;
  @Input() year:number;
  @Input() month:number;

  lateAircraftTime:boolean = false;
  carrierTime:boolean = false;
  weatherTime:boolean = false;
  securityTime:boolean = false;
  nationalAviationSystemTime:boolean = false;
  totalTime:boolean = false;

  content:any = [];

  constructor(public rest:RestService) { }

  ngOnInit() {
  }

  updateLateAircraftTime(event) {
    this.lateAircraftTime = event.target.checked;
  }

  updateCarrierTime(event) {
    this.carrierTime = event.target.checked;
  }

  updateWeatherTime(event) {
    this.weatherTime = event.target.checked;
  }

  updateSecurityTime(event) {
    this.securityTime = event.target.checked;
  }

  updateNationalAviationSystemTime(event) {
    this.nationalAviationSystemTime = event.target.checked;
  }

  updateTotalTime(event) {
    this.totalTime = event.target.checked;
  }

  getDelayTimeData() {
      this.rest.getDelayTimeData(this.airportCode, this.year, this.month, this.lateAircraftTime, this.carrierTime,
                                 this.weatherTime, this.securityTime, this.nationalAviationSystemTime, this.totalTime).subscribe(data => {
        console.log(data);
        this.content = data.content;
      });
  }

}
