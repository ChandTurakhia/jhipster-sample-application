jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILocationHeader, LocationHeader } from '../location-header.model';
import { LocationHeaderService } from '../service/location-header.service';

import { LocationHeaderRoutingResolveService } from './location-header-routing-resolve.service';

describe('Service Tests', () => {
  describe('LocationHeader routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: LocationHeaderRoutingResolveService;
    let service: LocationHeaderService;
    let resultLocationHeader: ILocationHeader | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(LocationHeaderRoutingResolveService);
      service = TestBed.inject(LocationHeaderService);
      resultLocationHeader = undefined;
    });

    describe('resolve', () => {
      it('should return ILocationHeader returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLocationHeader = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLocationHeader).toEqual({ id: 123 });
      });

      it('should return new ILocationHeader if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLocationHeader = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultLocationHeader).toEqual(new LocationHeader());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLocationHeader = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLocationHeader).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
