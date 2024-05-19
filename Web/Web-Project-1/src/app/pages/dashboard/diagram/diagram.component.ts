import { Component } from '@angular/core';
import { Router } from '@angular/router';
import * as Highcharts from 'highcharts';
import { SessionService } from 'src/app/services/session.service';

@Component({
  selector: 'app-diagram',
  templateUrl: './diagram.component.html',
  styleUrls: ['./diagram.component.scss']
})
export class DiagramComponent {

  chartTitle: string = 'Participant Status';
  chartSubTitle: string = 'Vote Count';
  chartData: [string, number][] = [['Need Help',0], ['In Progress',0], ['done',0]];
  chartBarColors: string[] = ['#F24A72','#FDAF75','#7AD67D'];

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
    subtitle: {
      text: this.chartSubTitle
    },
    tooltip: {
      enabled: false
    },
    legend: {
      enabled: false
    },
    xAxis:{
      categories: undefined,
      type: "category"
    },
    yAxis : {
       min: 0, 
       title: undefined,
       allowDecimals: false,
       labels: {
          overflow: 'justify'
       }
    },
    plotOptions : {
       bar: {
          dataLabels: {
             enabled: true
          }
       }
    },
    credits:{
       enabled: false
    },
    series: [
       {
          name: undefined,
          data: this.chartData,
          type: 'bar',
          colorByPoint:true,
          colors: this.chartBarColors,
       }
    ]
 };

 constructor(private sessionService: SessionService, private router: Router) {

 }

 ngOnInit(): void {
 
   this.sessionService.getUserRef()?.ref.onSnapshot((querySnapshot) => {
     
     querySnapshot.docs.forEach((userDoc: any) => {
        let user = userDoc.data()
        this.chartData = this.chartData.map(([key, value]) => {
          return [key, 0];
        });
        this.chartData = this.chartData.map(([key, value]) => {
          if (key === user.status) {
            return [key, value + 1];
          }
          return [key, value];
        });

        this.chartOptions.series![0] = {
          name: undefined,
          data: this.chartData,
          type: 'bar',
          colorByPoint:true,
          colors: this.chartBarColors,
       };
        this.updateFlag = true;
        
     })
   });

   
 }
 ngOnDestroy(): void {

 }
 
}
