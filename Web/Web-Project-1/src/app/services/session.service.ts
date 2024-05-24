import { Injectable } from '@angular/core';
import { Session } from './session';
import { AngularFirestore, AngularFirestoreCollection, AngularFirestoreDocument } from '@angular/fire/compat/firestore';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SessionService {


  sessionId: string | null;
  sessionName: string | undefined;
  surveyExists: boolean = false;
  sessionRef: AngularFirestoreDocument | undefined;
  userRef: AngularFirestoreCollection | undefined;
  sessionStorageKey: string = "UNIfy_Session_Id";

  constructor(private store: AngularFirestore) {
    this.sessionId = sessionStorage.getItem(this.sessionStorageKey);
    if (this.sessionId) {
      this.sessionRef = this.store.collection("Session").doc(this.sessionId);
      this.userRef = this.store.collection("Session").doc(this.sessionId).collection("User");

      this.sessionRef.ref.get().then((sessionDoc: any) => {
        let session = sessionDoc.data();
        let survey = session.survey;
        this.sessionName = session.name;
    
        this.surveyExists = survey != null;
      })
    }
  }

  getCurrentSessionId() {
    return this.sessionId;
  }

  getSessionName(){
    return this.sessionName;
  }

  getSessionRef() {
    return this.sessionRef;
  }

  getUserRef() {
    return this.userRef;
  }

  setSurveyExists(value: boolean){
    this.surveyExists = value;
  }

  getSurveyExists() {
    return this.surveyExists;
  }

  deleteSession() {

    let deleteSession = this.sessionRef!.ref.delete();


    return Promise.all([deleteSession]).then(() => {
      this.sessionRef = undefined;
      sessionStorage.removeItem(this.sessionStorageKey);
      this.sessionId = null;

      this.userRef = undefined;
    });
  }

  createSession(sessionName: string): Promise<any> {

    this.sessionName = sessionName;
    return this.store.collection("Session").add({ name: sessionName, survey: null }).then((ref: any) => {

      this.sessionId = ref.id;
      sessionStorage.setItem(this.sessionStorageKey, this.sessionId!);

      this.sessionRef = this.store.collection("Session").doc(ref.id);
      this.userRef = this.store.collection("Session").doc(ref.id).collection("User");
    })
  }
}
