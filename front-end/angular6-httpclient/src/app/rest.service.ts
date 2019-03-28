import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { map, catchError, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class RestService {

  constructor(private http: HttpClient) { }

  const endpoint = 'http://localhost:3000/api/v1/';
  const httpOptions = {
    headers: new HttpHeaders({
      'Content-Type':  'application/json'
    })
  };

  private extractData(res: Response) {
    let body = res;
    return body || { };
  }

  getAirports(): Observable<any> {
    return this.http.get(endpoint + 'airports').pipe(
      map(this.extractData));
  }

  getCarriers(): Observable<any> {
    return this.http.get(endpoint + 'carriers').pipe(
      map(this.extractData));
  }

  getAirport(airportCode): Observable<any> {
    return this.http.get(endpoint + 'airports/' + airportCode + '/').pipe(
      map(this.extractData));
  }

  getCarriersAtAirport(airportCode): Observable<any> {
    return this.http.get(endpoint + 'airports/' + airportCode + '/carriers/').pipe(
      map(this.extractData));
  }

  getAirportCarrier(airportCode, carrierCode): Observable<any> {
    return this.http.get(endpoint + 'airports/' + airportCode + '/carriers/' + carrierCode + '/').pipe(
      map(this.extractData));
  }

  getStats(airportCode, carrierCode): Observable<any> {
    return this.http.get(endpoint + 'airports/' + airportCode + '/carriers/' + carrierCode + '/stats/').pipe(
      map(this.extractData));
  }

  addStats(data): Observable<any> {
    console.log(data);
    return this.http.post<any>(endpoint + 'airports/' + 'carriers/' + 'stats/', JSON.stringify(data), httpOptions).pipe(
      tap((data) => console.log(`added stats`)),
      catchError(this.handleError<any>('addStats'))
    );
  }

  updateStats(airportCode, carrierCode, data): Observable<any> {
    return this.http.put(endpoint + 'airports/' + airportCode + '/carriers' + carrierCode + '/stats/', JSON.stringify(data), httpOptions).pipe(
      tap(_ => console.log(`updated stats for airport code=${airportCode} and carrier code=${carrierCode}`)),
      catchError(this.handleError<any>('updateStats'))
    );
  }

  deleteStats(airportCode, carrierCode): Observable<any> {
    return this.http.delete<any>(endpoint + 'airports/' + airportCode + '/carriers' + carrierCode + '/stats/', httpOptions).pipe(
      tap(_ => console.log(`deleted stats for airport code=${airportCode} and carrier code=${carrierCode}`)),
      catchError(this.handleError<any>('deleteStats'))
    );
  }

  getFlightData(airportCode, carrierCode): Observable<any> {
    return this.http.get(endpoint + 'airports/' + airportCode + '/carriers/' + carrierCode + '/stats' + '/flight/').pipe(
      map(this.extractData));
  }

  getDelayTime(): Observable<any> {
    return this.http.get(endpoint + 'airports/' + '/carriers' + '/stats' + '/delay-time/').pipe(
      map(this.extractData));
  }

  getDelayTime(airportCode): Observable<any> {
    return this.http.get(endpoint + 'airports/' + airportCode + '/carriers' + '/stats' + '/delay-time/').pipe(
      map(this.extractData));
  }

  get2Airports(airportCode1, airportCode2): Observable<any> {
    return this.http.get(endpoint + 'airports/' + airportCode1 + '/' + airportCode2 + '/').pipe(
      map(this.extractData));
  }

  get2AirportsCarriers(airportCode1, airportCode2): Observable<any> {
    return this.http.get(endpoint + 'airports/' + airportCode1 + '/' + airportCode2 + '/carriers/').pipe(
      map(this.extractData));
  }

  get2AirportsCarrier(airportCode1, airportCode2, carrierCode): Observable<any> {
    return this.http.get(endpoint + 'airports/' + airportCode1 + '/' + airportCode2 + '/carriers/' + carrierCode +'/').pipe(
      map(this.extractData));
  }

  getExtraStats(airportCode1, airportCode2): Observable<any> {
    return this.http.get(endpoint + 'airports/' + airportCode1 + '/' + airportCode2 + '/carriers' +'/extra-stats/').pipe(
      map(this.extractData));
  }

  getExtraStats(airportCode1, airportCode2, carrierCode): Observable<any> {
    return this.http.get(endpoint + 'airports/' + airportCode1 + '/' + airportCode2 + '/carriers/' + carrierCode +'/extra-stats/').pipe(
      map(this.extractData));
  }

  private handleError<T> (operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      console.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }
  
}
