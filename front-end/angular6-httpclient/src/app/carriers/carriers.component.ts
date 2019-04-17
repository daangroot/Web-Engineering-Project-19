import { Component, Input, OnInit } from '@angular/core';
import { RestService } from '../rest.service';

@Component({
  selector: 'app-carriers',
  templateUrl: './carriers.component.html',
  styleUrls: ['./carriers.component.css']
})
export class CarriersComponent implements OnInit {

  carriersWithLinks:any = [];
  isError:boolean = false;
  error:string;

  @Input() carrierCode:string;

  constructor(public rest:RestService) { }

  ngOnInit() {
    this.getCarriers();
  }

  getCarriers() {
    this.rest.getCarriers().subscribe((data) => {
      console.log(data);
      this.carriersWithLinks = data.content;
      this.isError = false;
    }, err => {
      console.error(err);
      this.carriersWithLinks = [];
      this.error = 'Error ' + err.status + ': ' + err.error;
      this.isError = true;
    });
  }

  getCarriersAtAirport() {
    if (!this.carrierCode) {
      this.getCarriers();
    } else {
      this.rest.getCarriersAtAirport(this.carrierCode).subscribe((data) => {
        console.log(data);
        this.carriersWithLinks = data.content;
        this.isError = false;
      }, err => {
        console.error(err);
        this.carriersWithLinks = [];
        this.error = 'Error ' + err.status + ': ' + err.error;
        this.isError = true;
      });
    }
  }
}
