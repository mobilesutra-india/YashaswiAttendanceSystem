package seedcommando.com.yashaswi.notifications;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import seedcommando.com.yashaswi.R;

/**
 * Created by commando1 on 9/21/2017.
 */
public class NotificationFragment extends Fragment {

        ArrayList<NotificationPOJO> arrayList;
        ListView listView;

        public NotificationFragment() {
            // Required empty public constructor
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.notification_fragment, container, false);
            listView = rootView.findViewById(R.id.list_notification);

            //Log.e("data", MainActivity.arrayList.toString());
            setUpExpList();
            NotificationAdapter adapter = new NotificationAdapter(getActivity(), arrayList);
            listView.setAdapter(adapter);

            return rootView;
        }
        private void setUpExpList() {

            //listDataMembers= new HashMap<String, ArrayList<WeekReportPojo>>();
            // Adding province names and number of population as groups

            arrayList = new ArrayList<NotificationPOJO>();
            NotificationPOJO notificationPOJO = new NotificationPOJO();
            notificationPOJO.setLeavetype("PL");
            notificationPOJO.setTodate("21 MAY 17");
            notificationPOJO.setFromdate("21 MAY 17");
            notificationPOJO.setTime("10:00 AM");
            notificationPOJO.setBalance("03");
            notificationPOJO.setReason("Personal Work");
            notificationPOJO.setRemark("NA");

            arrayList.add(notificationPOJO);

            NotificationPOJO notificationPOJO1 = new NotificationPOJO();
            notificationPOJO1.setLeavetype("SL");
            notificationPOJO1.setTodate("24 MAY 17");
            notificationPOJO1.setFromdate("21 MAY 17");
            notificationPOJO1.setTime("09:00 PM");
            notificationPOJO1.setBalance("09");
            notificationPOJO1.setReason("Work");
            notificationPOJO1.setRemark("NA");

            arrayList.add(notificationPOJO1);
        }

    }

