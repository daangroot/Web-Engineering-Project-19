import { Component, OnInit, Input } from '@angular/core';
import { RestService } from '../rest.service';

@Component({
  selector: 'app-extra-statistics',
  templateUrl: './extra-statistics.component.html',
  styleUrls: ['./extra-statistics.component.css']
})
export class ExtraStatisticsComponent implements OnInit {
  content:any = [];

  @Input() airportCode1:string;
  @Input() airportCode2:string;
  @Input() carrierCode:string;

  constructor(public rest:RestService) { }

  ngOnInit() {
  }

  getExtraStats() {
    this.rest.getExtraStats(this.airportCode1, this.airportCode2, this.carrierCode).subscribe(data => {
      console.log(data);
      this.content = data.content;
    });
  }

}
