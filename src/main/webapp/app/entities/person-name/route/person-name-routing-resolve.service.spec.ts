jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPersonName, PersonName } from '../person-name.model';
import { PersonNameService } from '../service/person-name.service';

import { PersonNameRoutingResolveService } from './person-name-routing-resolve.service';

describe('Service Tests', () => {
  describe('PersonName routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PersonNameRoutingResolveService;
    let service: PersonNameService;
    let resultPersonName: IPersonName | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PersonNameRoutingResolveService);
      service = TestBed.inject(PersonNameService);
      resultPersonName = undefined;
    });

    describe('resolve', () => {
      it('should return IPersonName returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPersonName = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPersonName).toEqual({ id: 123 });
      });

      it('should return new IPersonName if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPersonName = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPersonName).toEqual(new PersonName());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPersonName = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPersonName).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
