jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPersonDetails, PersonDetails } from '../person-details.model';
import { PersonDetailsService } from '../service/person-details.service';

import { PersonDetailsRoutingResolveService } from './person-details-routing-resolve.service';

describe('Service Tests', () => {
  describe('PersonDetails routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PersonDetailsRoutingResolveService;
    let service: PersonDetailsService;
    let resultPersonDetails: IPersonDetails | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PersonDetailsRoutingResolveService);
      service = TestBed.inject(PersonDetailsService);
      resultPersonDetails = undefined;
    });

    describe('resolve', () => {
      it('should return IPersonDetails returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPersonDetails = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPersonDetails).toEqual({ id: 123 });
      });

      it('should return new IPersonDetails if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPersonDetails = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPersonDetails).toEqual(new PersonDetails());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPersonDetails = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPersonDetails).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
