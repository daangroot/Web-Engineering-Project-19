import { Component, OnInit } from '@angular/core';
import { RestService } from '../rest.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-extrastats-carrier',
  templateUrl: './extrastats-carrier.component.html',
  styleUrls: ['./extrastats-carrier.component.css']
})
export class ExtrastatsCarrierComponent implements OnInit {

  extrastats:any = [];

  constructor(public rest:RestService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit() {
    this.getExtrastats();
  }

  getExtrastats() {
    this.extrastats = [];
    this.rest.getExtraStats(this.route.snapshot.params['airportCode1', 'airportCode2', 'carrierCode']).subscribe((data: {}) => {
      console.log(data);
      this.extrastats = data;
    });
  }
}
