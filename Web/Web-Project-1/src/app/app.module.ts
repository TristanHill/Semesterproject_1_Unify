import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { initializeApp, provideFirebaseApp } from '@angular/fire/app';
import { getAuth, provideAuth } from '@angular/fire/auth';
import { getAnalytics, provideAnalytics, ScreenTrackingService, UserTrackingService } from '@angular/fire/analytics';
import { getFirestore, provideFirestore } from '@angular/fire/firestore';
import { getDatabase, provideDatabase } from '@angular/fire/database';
import { getFunctions, provideFunctions } from '@angular/fire/functions';
import { getMessaging, provideMessaging } from '@angular/fire/messaging';
import { getPerformance, providePerformance } from '@angular/fire/performance';
import { getStorage, provideStorage } from '@angular/fire/storage';
import { environment } from 'src/environments/environment';
import { AngularFirestoreModule } from '@angular/fire/compat/firestore';
import { AngularFireModule } from '@angular/fire/compat';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatSidenavModule } from '@angular/material/sidenav';
import { CreateSessionComponent } from './pages/create-session/create-session.component';
import { MatButtonModule } from '@angular/material/button';
import { QRCodeModule } from 'angularx-qrcode';
import { QrCodeComponent } from './pages/dashboard/qr-code/qr-code.component';
import { NotificationComponent } from './pages/dashboard/notification/notification.component';
import { NotificationItemComponent } from './pages/dashboard/notification/notification-item/notification-item.component';
import { QuestionComponent } from './pages/dashboard/question/question.component';
import { QuestionItemComponent } from './pages/dashboard/question/question-item/question-item.component';
import { SurveyComponent } from './pages/dashboard/survey/survey.component';
import { DiagramComponent } from './pages/dashboard/diagram/diagram.component';
import { HighchartsChartModule } from 'highcharts-angular';
import {MatInputModule} from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import {MatIconModule} from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatCardModule } from '@angular/material/card';


@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    CreateSessionComponent,
    QrCodeComponent,
    NotificationComponent,
    NotificationItemComponent,
    QuestionComponent,
    QuestionItemComponent,
    SurveyComponent,
    DiagramComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    AngularFireModule.initializeApp(environment.firebase),
    AngularFirestoreModule,
    QRCodeModule,
    HighchartsChartModule,
    MatInputModule,
    FormsModule,
    MatIconModule,
    MatListModule,
    MatCardModule,
    provideFirebaseApp(() => initializeApp({ "projectId": "semesterproject-1", "appId": "1:601327501979:web:5822d630b2685f2cab3c3e", "storageBucket": "semesterproject-1.appspot.com", "apiKey": "AIzaSyDclX8dRbeX42Q3wjAavWwSxkGCYucqO88", "authDomain": "semesterproject-1.firebaseapp.com", "messagingSenderId": "601327501979", "measurementId": "G-WEMNJBXDS7" })),
    provideAuth(() => getAuth()),
    provideAnalytics(() => getAnalytics()),
    provideFirestore(() => getFirestore()),
    provideDatabase(() => getDatabase()),
    provideFunctions(() => getFunctions()),
    provideMessaging(() => getMessaging()),
    providePerformance(() => getPerformance()),
    provideStorage(() => getStorage()),
    BrowserAnimationsModule,
    MatSidenavModule,
    MatButtonModule
  ],
  providers: [
    ScreenTrackingService,
    UserTrackingService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
