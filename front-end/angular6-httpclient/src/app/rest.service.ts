import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { map, catchError, tap, } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class RestService {

  constructor(private http: HttpClient) { }

  endpoint = 'http://localhost:8080/';
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type':  'application/json'
    })
  };

  private extractData(res: Response) {
    let body = res;
    return body || { };
  }

  getAirports(): Observable<any> {
    return this.http.get(this.endpoint + 'airports').pipe(
      map(this.extractData));
  }

  getCarriers(): Observable<any> {
    return this.http.get(this.endpoint + 'carriers').pipe(
      map(this.extractData));
  }

  getCarriersAtAirport(airportCode): Observable<any> {
    return this.http.get(this.endpoint + 'airports/' + airportCode + '/carriers/').pipe(
      map(this.extractData));
  }

  getStats(airportCode:string, carrierCode:string, year:number, month:number): Observable<any> {
    if (!year && !month) {
      return this.http.get(this.endpoint + 'airports/' + airportCode + '/carriers/' + carrierCode + '/stats').pipe(
        map(this.extractData));
    } else if (!month) {
      return this.http.get(this.endpoint + 'airports/' + airportCode + '/carriers/' + carrierCode + '/stats?year=' + year).pipe(
        map(this.extractData));
    } else if (!year) {
      return this.http.get(this.endpoint + 'airports/' + airportCode + '/carriers/' + carrierCode + '/stats?month=' + month).pipe(
        map(this.extractData));
    } else {
      return this.http.get(this.endpoint + 'airports/' + airportCode + '/carriers/' + carrierCode + '/stats?year=' + year + '&month=' + month).pipe(
        map(this.extractData));
    }
  }

  addStats(data): Observable<any> {
    return this.http.post<any>(this.endpoint + 'airports/' + 'carriers/' + 'stats/', data, this.httpOptions).pipe(
      tap(() => console.log(`added stats`)),
      catchError(this.handleError<any>('addStats'))
    );
  }

  updateStats(airportCode:string, carrierCode:string, year:number, month:number, data): Observable<any> {
    if (!year && !month) {
      return this.http.put(this.endpoint + 'airports/' + airportCode + '/carriers/' + carrierCode + '/stats', data, this.httpOptions).pipe(
        tap(_ => console.log(`updated stats for airport code=${airportCode} and carrier code=${carrierCode}`)),
        catchError(this.handleError('updateStats'))
      );
    } else if (!month) {
      return this.http.put(this.endpoint + 'airports/' + airportCode + '/carriers/' + carrierCode + '/stats?year=' + year, data, this.httpOptions).pipe(
        tap(_ => console.log(`updated stats for airport code=${airportCode} and carrier code=${carrierCode}`)),
        catchError(this.handleError('updateStats'))
      );
    } else if (!year) {
      return this.http.put(this.endpoint + 'airports/' + airportCode + '/carriers/' + carrierCode + '/stats?month=' + month, data, this.httpOptions).pipe(
        tap(_ => console.log(`updated stats for airport code=${airportCode} and carrier code=${carrierCode}`)),
        catchError(this.handleError('updateStats'))
      );
    } else {
      return this.http.put(this.endpoint + 'airports/' + airportCode + '/carriers/' + carrierCode + '/stats?year=' + year + '&month=' + month, data, this.httpOptions).pipe(
        tap(_ => console.log(`updated stats for airport code=${airportCode} and carrier code=${carrierCode}`)),
        catchError(this.handleError('updateStats'))
      );
    }
  }

  deleteStats(airportCode:string, carrierCode:string, year:number, month:number): Observable<any> {
    if (!year && !month) {
      return this.http.delete(this.endpoint + 'airports/' + airportCode + '/carriers/' + carrierCode + '/stats').pipe(
        tap(_ => console.log(`deleted stats for airport code=${airportCode} and carrier code=${carrierCode}`)),
        catchError(this.handleError('deleteStats'))
      );
    } else if (!month) {
      return this.http.delete(this.endpoint + 'airports/' + airportCode + '/carriers/' + carrierCode + '/stats?year=' + year).pipe(
        tap(_ => console.log(`deleted stats for airport code=${airportCode} and carrier code=${carrierCode}`)),
        catchError(this.handleError('deleteStats'))
      );
    } else if (!year) {
      return this.http.delete(this.endpoint + 'airports/' + airportCode + '/carriers/' + carrierCode + '/stats?month=' + month).pipe(
        tap(_ => console.log(`deleted stats for airport code=${airportCode} and carrier code=${carrierCode}`)),
        catchError(this.handleError('deleteStats'))
      );
    } else {
      return this.http.delete<any>(this.endpoint + 'airports/' + airportCode + '/carriers/' + carrierCode + '/stats?year=' + year + '&month=' + month).pipe(
          tap(_ => console.log(`deleted stats for airport code=${airportCode} and carrier code=${carrierCode}`)),
          catchError(this.handleError('deleteProduct'))
        );
    }
  }

  getFlightData(airportCode:string, carrierCode:string, year:number, month:number): Observable<any> {
    if (!year && !month) {
      return this.http.get(this.endpoint + 'airports/' + airportCode + '/carriers/' + carrierCode + '/stats/flight').pipe(
        map(this.extractData));
    } else if (!month) {
      return this.http.get(this.endpoint + 'airports/' + airportCode + '/carriers/' + carrierCode + '/stats/flight?year=' + year).pipe(
        map(this.extractData));
    } else if (!year) {
      return this.http.get(this.endpoint + 'airports/' + airportCode + '/carriers/' + carrierCode + '/stats/flight?month=' + month).pipe(
        map(this.extractData));
    } else {
      return this.http.get(this.endpoint + 'airports/' + airportCode + '/carriers/' + carrierCode + '/stats/flight?year=' + year + '&month=' + month).pipe(
        map(this.extractData));
    }
  }

  getDelayTimeData(airportCode:string, year:number, month:number, lateAircraft:boolean, carrier:boolean, weather:boolean,
                   security:boolean, nationalAviationSystem:boolean,total:boolean): Observable<any> {
    let url = this.endpoint + 'airports/carriers/stats/delay-time?';
    if (airportCode) {
      url = this.endpoint + 'airports/' + airportCode + '/carriers/stats/delay-time?';
    }

    let prefix = false;

    if (year && month) {
      url += 'year=' + year + '&month=' + month;
      prefix = true;
    } else if (year && !month) {
      url += 'year=' + year;
      prefix = true;
    } else if (!year && month) {
      url += 'month=' + month;
      prefix = true;
    }

    if (prefix && (lateAircraft || carrier || weather || security || nationalAviationSystem || total)) {
      url += '&type=';
    }

    let previousType = false;

    if (lateAircraft) {
      url += 'late-aircraft';
      previousType = true;
    }

    if (carrier) {
      if (previousType) {
        url += ',carrier';
      } else {
        url += 'carrier';
        previousType = true;
      }
    }

    if (weather) {
      if (previousType) {
        url += ',weather';
      } else {
        url += 'weather';
        previousType = true;
      }
    }

    if (security) {
      if (previousType) {
        url += ',security';
      } else {
        url += 'security';
        previousType = true;
      }
    }

    if (nationalAviationSystem) {
      if (previousType) {
        url += ',national-aviation-system';
      } else {
        url += 'national-aviation-system';
        previousType = true;
      }
    }

    if (total) {
      if (previousType) {
        url += ',total';
      } else {
        url += 'total';
        previousType = true;
      }
    }

    return this.http.get(url).pipe(
        map(this.extractData));
  }

  getExtraStats(airportCode1:string, airportCode2:string, carrierCode:string): Observable<any> {
    let url = this.endpoint + 'airports/' + airportCode1 + '/' + airportCode2 + '/carriers/extra-stats';
    if (carrierCode) {
      url = this.endpoint + 'airports/' + airportCode1 + '/' + airportCode2 + '/carriers/' + carrierCode + '/extra-stats';
    }

    return this.http.get(url).pipe(
      map(this.extractData));
  }

  getExtraStatsCarrier(airportCode1, airportCode2, carrierCode): Observable<any> {
    return this.http.get(this.endpoint + 'airports/' + airportCode1 + '/' + airportCode2 + '/carriers/' + carrierCode +'/extra-stats/').pipe(
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
