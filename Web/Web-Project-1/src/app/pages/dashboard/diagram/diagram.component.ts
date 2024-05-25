import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import * as Highcharts from 'highcharts';
import { SessionService } from 'src/app/services/session.service';

@Component({
   selector: 'app-diagram',
   templateUrl: './diagram.component.html',
   styleUrls: ['./diagram.component.scss']
})
export class DiagramComponent implements OnInit, OnDestroy {
   isReady: boolean = false;
   isSurvey: boolean = false;
   surveyOptions: string[] = [];

   chartTitle: string = '';
   //chartSubTitle: string = '';
   chartData: [string, number][] = [];
   chartBarColors: string[] | undefined = undefined;

   /* Highcharts Stuff */
   isHighcharts = typeof Highcharts === 'object';
   updateFlag = false;
   Highcharts: typeof Highcharts = Highcharts;
   chartOptions: Highcharts.Options = {
      chart: {
         type: 'bar'
      },
      title: {
         text: this.chartTitle
      },
     // subtitle: {
     //    text: this.chartSubTitle
    //  },
      tooltip: {
         enabled: false
      },
      legend: {
         enabled: false
      },
      xAxis: {
         categories: undefined,
         type: "category"
      },
      yAxis: {
         min: 0,
         title: undefined,
         allowDecimals: false,
         labels: {
            overflow: 'justify'
         }
      },
      plotOptions: {
         bar: {
            dataLabels: {
               enabled: true
            }
         }
      },
      credits: {
         enabled: false
      },
      series: [
         {
            name: undefined,
            data: this.chartData,
            type: 'bar',
            colorByPoint: true,
            colors: this.chartBarColors,
         }
      ]
   };

   constructor(private sessionService: SessionService, private router: Router) {
      this.isSurvey = router.url == "/dashboard/survey/result";
   }

   ngOnInit(): void {

      this.sessionService.getSessionRef()?.get().subscribe((session: any) => {

         let survey = session.data().survey;


         if (this.isSurvey && survey) {
            this.chartTitle = survey.surveyTitle;
           // this.chartSubTitle = 'Vote Count';
            this.surveyOptions = survey.surveyOptions;
            this.chartData = survey.surveyOptions.map((key: string) => {
               return [key, 0];
            });
            this.chartBarColors = undefined;

         } else {
            this.chartTitle = 'Participant Status';
          //  this.chartSubTitle = 'Vote Count';
            this.chartData = [['Need Help', 0], ['In Progress', 0], ['Done', 0]];
            this.chartBarColors = ['#F24A72', '#FDAF75', '#7AD67D'];
         }

         this.sessionService.getUserRef()?.valueChanges().subscribe((userList: any) => {
            this.chartData = this.chartData.map(([key, value]) => {
               return [key, 0];
            });
            userList.forEach((user: any) => {
               this.chartData = this.chartData.map(([key, value]) => {
                  if (!this.isSurvey && key === user.status) {
                     return [key, value + 1];
                  }

                  if (this.isSurvey && user.surveyOption != null && key === this.surveyOptions[user.surveyOption]) {
                     return [key, value + 1];
                  }
                  return [key, value];
               });
            })
            console.log(this.chartData)
            this.updateChart();
            this.isReady = true;
         });

      })
   }



   updateChart() {
      this.chartOptions.title = {
         text: this.chartTitle
      }
      //this.chartOptions.subtitle = {
      //   text: this.chartSubTitle
      //}
      this.chartOptions.series![0] = {
         name: undefined,
         data: this.chartData,
         type: 'bar',
         colorByPoint: true,
         colors: this.chartBarColors,
      };
      this.updateFlag = true;
   }

   ngOnDestroy(): void {

   }

   newSurvey() {
      this.sessionService.getSessionRef()?.update({
         survey: null
      }).then(() => {
         this.sessionService.setSurveyExists(false);
         this.router.navigate(['/dashboard/survey']);
      })
   }

}
