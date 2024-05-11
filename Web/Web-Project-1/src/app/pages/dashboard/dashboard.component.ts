import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SessionService } from 'src/app/services/session.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit, OnDestroy {
  
    content = "halla";

    constructor(private sessionService: SessionService, private router: Router){

    }

    ngOnInit(): void {
      
    }
    ngOnDestroy(): void {
      
    }

   
}
