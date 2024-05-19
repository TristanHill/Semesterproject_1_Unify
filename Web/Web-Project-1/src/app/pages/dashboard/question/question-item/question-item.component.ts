import { Component, Input } from '@angular/core';
import { Question } from '../model/question';

@Component({
  selector: 'app-question-item',
  templateUrl: './question-item.component.html',
  styleUrls: ['./question-item.component.scss']
})
export class QuestionItemComponent {
  @Input() question!: Question;
}
