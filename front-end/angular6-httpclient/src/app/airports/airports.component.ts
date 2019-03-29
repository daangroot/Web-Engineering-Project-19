import { Component, OnInit } from '@angular/core';
import { RestService } from '../rest.service';

@Component({
  selector: 'app-airports',
  templateUrl: './airports.component.html',
  styleUrls: ['./airports.component.css']
})
export class AirportsComponent implements OnInit {

  airportsWithLinks:any = [];
  isError:boolean = false;
  error:string;

  constructor(public rest:RestService) { }

  ngOnInit() {
    this.getAirports();
  }

  getAirports() {
    this.rest.getAirports().subscribe((data) => {
      console.log(data);
      this.airportsWithLinks = data.content;
    }, err => {
      console.error(err);
      this.airportsWithLinks = [];
      this.error = 'Error ' + err.status + ': ' + err.error;
      this.isError = true;
    });
  }
}
