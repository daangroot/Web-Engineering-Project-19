import { Component, OnInit } from '@angular/core';
import { RestService } from '../rest.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-carriers-airport',
  templateUrl: './carriers-airport.component.html',
  styleUrls: ['./carriers-airport.component.css']
})
export class CarriersAirportComponent implements OnInit {

  carriers:any = [];

  constructor(public rest:RestService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit() {
    this.getCarriers();
  }

  getCarriers() {
    this.carriers = [];
    this.rest.getCarriersAtAirport(this.route.snapshot.params['airportCode']).subscribe((data: {}) => {
      console.log(data);
      this.carriers = data;
    });
  }
}
