jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAddressHeader, AddressHeader } from '../address-header.model';
import { AddressHeaderService } from '../service/address-header.service';

import { AddressHeaderRoutingResolveService } from './address-header-routing-resolve.service';

describe('Service Tests', () => {
  describe('AddressHeader routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: AddressHeaderRoutingResolveService;
    let service: AddressHeaderService;
    let resultAddressHeader: IAddressHeader | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(AddressHeaderRoutingResolveService);
      service = TestBed.inject(AddressHeaderService);
      resultAddressHeader = undefined;
    });

    describe('resolve', () => {
      it('should return IAddressHeader returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAddressHeader = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAddressHeader).toEqual({ id: 123 });
      });

      it('should return new IAddressHeader if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAddressHeader = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultAddressHeader).toEqual(new AddressHeader());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAddressHeader = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAddressHeader).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
