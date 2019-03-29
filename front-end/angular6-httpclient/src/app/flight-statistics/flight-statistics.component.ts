import { Component, OnInit, Input } from '@angular/core';
import { RestService } from '../rest.service';

@Component({
  selector: 'app-flight-statistics',
  templateUrl: './flight-statistics.component.html',
  styleUrls: ['./flight-statistics.component.css']
})
export class FlightStatisticsComponent implements OnInit {

  @Input() airportCode:string;
  @Input() carrierCode:string;
  @Input() year:number;
  @Input() month:number;

  flightData:any = [];

  constructor(public rest:RestService) { }

  ngOnInit() {
  }

  getFlightData() {
    if (this.airportCode && this.carrierCode) {
      this.rest.getStats(this.airportCode, this.carrierCode, this.year, this.month).subscribe(data => {
        console.log(data);
        this.flightData = data.content;
      });
    }
  }

}
