import { Component, Input } from '@angular/core';
import { RestService } from '../rest.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-stats',
  templateUrl: './stats.component.html',
  styleUrls: ['./stats.component.css']
})
export class StatsComponent {

  addUpdate:boolean = false;

  statistics:any = [];

  cancelledCount:number = 0;
  onTimeCount:number = 0;
  delayedCount:number = 0;
  divertedCount:number = 0;
  totalCount:number = 0;

  lateAircraftCount:number = 0;
  carrierCount: number = 0;
  weatherCount: number = 0;
  securityCount: number = 0;
  nationalAviationSystemCount: number = 0;

  lateAircraftTime:number = 0;
  carrierTime: number = 0;
  weatherTime: number = 0;
  securityTime: number = 0;
  nationalAviationSystemTime: number = 0;
  totalTime: number = 0;

  @Input() airportCode:string;
  @Input() carrierCode:string;
  @Input() year:number;
  @Input() month:number;

  constructor(public rest:RestService) { }

  private toJson() {
    let  json = [
                  {
                    year: this.year,
                    month: this.month,
                    airport: {
                      code: this.airportCode
                    },
                    carrier: {
                      code: this.carrierCode
                    },
                    flightData: {
                      cancelledCount: this.cancelledCount,
                      onTimeCount: this.onTimeCount,
                      delayedCount: this.delayedCount,
                      divertedCount: this.divertedCount,
                      totalCount: this.totalCount
                    },
                    delayData: {
                      lateAircraftCount: this.lateAircraftCount,
                      carrierCount: this.carrierCount,
                      weatherCount: this.weatherCount,
                      securityCount: this.securityCount,
                      nationalAviationSystemCount: this.nationalAviationSystemCount
                    },
                    delayTimeData: {
                      lateAircraftTime: this.lateAircraftTime,
                      carrierTime: this.carrierTime,
                      weatherTime: this.weatherTime,
                      securityTime: this.securityTime,
                      nationalAviationSystemTime: this.nationalAviationSystemTime,
                      totalTime: this.totalTime
                    }
                  }
                ];
    return json;
  }

  addUpdateStats(event) {
    this.addUpdate = event.target.checked;
  }

  getStatistics() {
    if (this.airportCode && this.carrierCode) {
      this.rest.getStats(this.airportCode, this.carrierCode, this.year, this.month).subscribe(data => {
        console.log(data);
        this.statistics = data.content;
      });
    }
  }

  addStatistics() {
    this.rest.addStats(this.toJson()).subscribe();
  }

  updateStatistics() {
    if (this.airportCode && this.carrierCode) {
      this.rest.updateStats(this.airportCode, this. carrierCode, this.year, this.month, this.toJson()).subscribe();
    }
  }

  deleteStatistics() {
    if (this.airportCode && this.carrierCode) {
      this.rest.deleteStats(this.airportCode, this.carrierCode, this.year, this.month).subscribe();
    }
  }
}
