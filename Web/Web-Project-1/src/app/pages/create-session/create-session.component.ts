import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { SessionService } from 'src/app/services/session.service';

@Component({
  selector: 'app-create-session',
  templateUrl: './create-session.component.html',
  styleUrls: ['./create-session.component.scss']
})
export class CreateSessionComponent {
  sessionName: string = "";

  constructor(private sessionService: SessionService, private router: Router){

  }

  createSession() {
    this.sessionName = this.sessionName.trim().length == 0 ? "Unify Session" : this.sessionName;
    this.sessionService.createSession(this.sessionName).then((_:any) => {
      this.router.navigate(['/dashboard'])
    });
    
  }
}
