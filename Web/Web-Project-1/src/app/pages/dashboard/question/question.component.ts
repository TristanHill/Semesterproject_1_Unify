import { Component } from '@angular/core';
import { Question } from './model/question';

@Component({
  selector: 'app-question',
  templateUrl: './question.component.html',
  styleUrls: ['./question.component.scss']
})
export class QuestionComponent {
    questionList: Question[] = [];
}
