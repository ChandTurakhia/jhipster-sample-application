import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'person',
        data: { pageTitle: 'myApp.person.home.title' },
        loadChildren: () => import('./person/person.module').then(m => m.PersonModule),
      },
      {
        path: 'person-details',
        data: { pageTitle: 'myApp.personDetails.home.title' },
        loadChildren: () => import('./person-details/person-details.module').then(m => m.PersonDetailsModule),
      },
      {
        path: 'person-name',
        data: { pageTitle: 'myApp.personName.home.title' },
        loadChildren: () => import('./person-name/person-name.module').then(m => m.PersonNameModule),
      },
      {
        path: 'person-address',
        data: { pageTitle: 'myApp.personAddress.home.title' },
        loadChildren: () => import('./person-address/person-address.module').then(m => m.PersonAddressModule),
      },
      {
        path: 'address-header',
        data: { pageTitle: 'myApp.addressHeader.home.title' },
        loadChildren: () => import('./address-header/address-header.module').then(m => m.AddressHeaderModule),
      },
      {
        path: 'location-header',
        data: { pageTitle: 'myApp.locationHeader.home.title' },
        loadChildren: () => import('./location-header/location-header.module').then(m => m.LocationHeaderModule),
      },
      {
        path: 'person-language',
        data: { pageTitle: 'myApp.personLanguage.home.title' },
        loadChildren: () => import('./person-language/person-language.module').then(m => m.PersonLanguageModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
