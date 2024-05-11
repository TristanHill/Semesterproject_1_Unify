import { Injectable } from '@angular/core';
import { Session } from './session';
import { AngularFirestore, AngularFirestoreCollection, AngularFirestoreDocument } from '@angular/fire/compat/firestore';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SessionService {
  

  sessionId: string | null;
  sessionRef: AngularFirestoreDocument | undefined;
  userRef: AngularFirestoreCollection | undefined;
  sessionStorageKey: string = "UNIfy_Session_Id";

  constructor(private store: AngularFirestore) { 
    this.sessionId = sessionStorage.getItem(this.sessionStorageKey);
    if(this.sessionId){
      this.sessionRef = this.store.collection("Session").doc(this.sessionId);
    }
  }

  getCurrentSessionId() {
   return this.sessionId;
  }

  getSessionRef(){
    return this.sessionRef;
  }

  getUserRef(){
    return this.userRef;
  }

  deleteSession(){
   
    let deleteSession = this.sessionRef!.ref.delete();
    
    
    return Promise.all([deleteSession]).then(() => {
      this.sessionRef = undefined;
      sessionStorage.removeItem(this.sessionStorageKey);
      this.sessionId = null;

      this.userRef = undefined;
    });
  }

  createSession(): Promise<any> {
    

    return this.store.collection("Session").add({name: "Session1"}).then((ref:any) => {
     
      this.sessionId = ref.id;
      sessionStorage.setItem(this.sessionStorageKey, this.sessionId!);
      
      this.sessionRef = this.store.collection("Session").doc(ref.id);
      this.userRef = this.store.collection("Session").doc(ref.id).collection("User");
    })
  }
}
