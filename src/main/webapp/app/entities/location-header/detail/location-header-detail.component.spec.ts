import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LocationHeaderDetailComponent } from './location-header-detail.component';

describe('Component Tests', () => {
  describe('LocationHeader Management Detail Component', () => {
    let comp: LocationHeaderDetailComponent;
    let fixture: ComponentFixture<LocationHeaderDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LocationHeaderDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ locationHeader: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LocationHeaderDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LocationHeaderDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load locationHeader on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.locationHeader).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
