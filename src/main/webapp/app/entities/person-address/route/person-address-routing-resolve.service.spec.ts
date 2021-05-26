jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPersonAddress, PersonAddress } from '../person-address.model';
import { PersonAddressService } from '../service/person-address.service';

import { PersonAddressRoutingResolveService } from './person-address-routing-resolve.service';

describe('Service Tests', () => {
  describe('PersonAddress routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PersonAddressRoutingResolveService;
    let service: PersonAddressService;
    let resultPersonAddress: IPersonAddress | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PersonAddressRoutingResolveService);
      service = TestBed.inject(PersonAddressService);
      resultPersonAddress = undefined;
    });

    describe('resolve', () => {
      it('should return IPersonAddress returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPersonAddress = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPersonAddress).toEqual({ id: 123 });
      });

      it('should return new IPersonAddress if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPersonAddress = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPersonAddress).toEqual(new PersonAddress());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPersonAddress = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPersonAddress).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
