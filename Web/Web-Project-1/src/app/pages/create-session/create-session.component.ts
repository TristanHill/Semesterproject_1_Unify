import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { SessionService } from 'src/app/services/session.service';

@Component({
  selector: 'app-create-session',
  templateUrl: './create-session.component.html',
  styleUrls: ['./create-session.component.scss']
})
export class CreateSessionComponent {

  constructor(private sessionService: SessionService, private router: Router){

  }

  createSession() {
    this.sessionService.createSession().then((_:any) => {
      this.router.navigate(['/dashboard'])
    });
    
  }
}
