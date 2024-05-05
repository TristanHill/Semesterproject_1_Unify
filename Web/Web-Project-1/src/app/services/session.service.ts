import { Injectable } from '@angular/core';
import { Session } from './session';
import { AngularFirestore } from '@angular/fire/compat/firestore';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SessionService {
  

  sessionId: number | undefined;
  sessionRef: Observable<any> | undefined;

  constructor(private store: AngularFirestore) { }

  getCurrentSessionId() {
   return this.sessionId;
  }

  getSessionRef(){
    return this.sessionRef;
  }

  createSession(): Promise<any> {
    console.log("!!!!!!!!!!!!!!!!!!!!!!!")

    return this.store.collection("Session").add({name: "Session1", participantCount: 0}).then((ref:any) => {
     
      this.sessionId = ref.id;
      this.sessionRef = this.store.collection("Session").doc(ref.id).snapshotChanges();
    })
  }
}
