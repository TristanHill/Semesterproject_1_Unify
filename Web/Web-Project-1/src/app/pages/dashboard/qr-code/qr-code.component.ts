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
    this.sessionService.getSessionRef()?.snapshotChanges().subscribe((ref: any) => {

    })

    this.sessionService.getUserRef()?.ref.onSnapshot((querySnapshot) => {
      
      this.activePartisipants = querySnapshot.size;
    });

    
  }
  ngOnDestroy(): void {

  }

  getCurrentSessionId(): string {
    return this.sessionService.getCurrentSessionId()!;
  }

  deleteSession() {
    this.sessionService.deleteSession().then(() => {
      this.router.navigate(['/create-session']);
    })
  }
}
