import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SessionService } from 'src/app/services/session.service';

@Component({
  selector: 'app-qr-code',
  templateUrl: './qr-code.component.html',
  styleUrls: ['./qr-code.component.scss']
})
export class QrCodeComponent implements OnInit, OnDestroy {
 
  activePartisipants: number = 0;

  constructor(private sessionService: SessionService, private router: Router) {

  }

  ngOnInit(): void {
  
    this.sessionService.getUserRef()?.valueChanges().subscribe((snapshot) => {
      
      this.activePartisipants = snapshot.length;
    });

    
  }
  ngOnDestroy(): void {

  }

  getCurrentSessionId(): string {
    return this.sessionService.getCurrentSessionId()!;
  }

  
}
