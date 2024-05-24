import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SessionService } from 'src/app/services/session.service';

@Component({
  selector: 'app-survey',
  templateUrl: './survey.component.html',
  styleUrls: ['./survey.component.scss']
})
export class SurveyComponent implements OnInit, OnDestroy {
  surveyTitle: string = "";
  surveyOptions: string[] = ["", ""];

  constructor(private sessionService: SessionService, private router: Router){
    
  }
  ngOnInit(): void {
  }
  ngOnDestroy(): void {
    
  }

  addOption() {
    this.surveyOptions.push("");
  }

  removeOption(index: number) {
    this.surveyOptions.splice(index,1);
  }

  trackByIndex(index: number, item: any): any {
    return index;
  }

  submit() {
    this.sessionService.getSessionRef()?.update({
      survey: {
        surveyTitle: this.surveyTitle != "" ? this.surveyTitle : "Survey Results",
        surveyOptions: this.surveyOptions.map((key: string, index: number) => {
          return key != "" ? key : "Option "+ (index+1);
        })
      }
    }).then(() => {
      this.sessionService.setSurveyExists(true);
      this.router.navigate(['/dashboard/survey/result']);
    })
  }
}
