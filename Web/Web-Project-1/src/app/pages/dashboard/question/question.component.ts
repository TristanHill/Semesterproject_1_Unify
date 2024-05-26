import { Component } from '@angular/core';
import { Question } from './model/question';
import { Router } from '@angular/router';
import { SessionService } from 'src/app/services/session.service';

@Component({
  selector: 'app-question',
  templateUrl: './question.component.html',
  styleUrls: ['./question.component.scss']
})
export class QuestionComponent {
  questionList: Question[] = [];
  constructor(private sessionService: SessionService, private router: Router) {

  }

  ngOnInit(): void {

    this.sessionService.getSessionRef()?.valueChanges().subscribe((session: any) => {
      console.log(session)
      this.questionList = session.questionList;
      this.questionList.reverse();
    })
  }

  ngOnDestroy(): void {

  }
}
