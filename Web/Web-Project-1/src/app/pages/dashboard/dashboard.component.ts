import { Component, OnDestroy, OnInit } from '@angular/core';
import { SessionService } from 'src/app/services/session.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit, OnDestroy {
  
    content = "halla";

    constructor(private sessionService: SessionService){

    }

    ngOnInit(): void {
      this.sessionService.getSessionRef()?.subscribe((ref: any) => {
        console.log("dashboard oninit");
        
        console.log(ref);
        console.log(ref.payload.data());
        this.content = ref.payload.data().participantCount;
      })
    }
    ngOnDestroy(): void {
      
    }
}
