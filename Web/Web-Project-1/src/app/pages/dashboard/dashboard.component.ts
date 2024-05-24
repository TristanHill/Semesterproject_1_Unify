import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SessionService } from 'src/app/services/session.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit, OnDestroy {

    constructor(private sessionService: SessionService, private router: Router){

    }

    ngOnInit(): void {
      
    }
    ngOnDestroy(): void {
      
    }

    getSessionName(){
      return this.sessionService.getSessionName();
    }

    deleteSession() {
      this.sessionService.deleteSession().then(() => {
        this.router.navigate(['/create-session']);
      })
    }

   
}
